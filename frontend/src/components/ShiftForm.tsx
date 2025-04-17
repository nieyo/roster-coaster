import {DatePicker, Divider, Form, FormInstance, Input, Select, TimePicker} from "antd";
import dayjs, {Dayjs} from "dayjs";
import React, {useEffect} from "react";
import {ShiftFormValues} from "../types/form/ShiftFormValues.ts";
import {ShiftDTO} from "../types/dto/ShiftDTO.ts";

interface ShiftFormProps {
    mode: string,
    onSubmit: (values: ShiftFormValues) => void,
    form: FormInstance,
    findShiftById?: (id: string) => ShiftDTO | undefined,
    id: React.Key[] | undefined,
    shifts: ShiftDTO[]
}

export default function ShiftForm(props: Readonly<ShiftFormProps>) {
    const {mode, form, onSubmit, findShiftById, id} = props;

    useEffect(() => {
        if (mode === "EDIT_SHIFT" && id && findShiftById) {
            const shift = findShiftById(id.toString());
            if (shift) {
                form.setFieldsValue({
                    formName: "EDIT_SHIFT",
                    id: shift.id,
                    tomorrow: dayjs(shift.duration.start).startOf('day'),
                    duration: [
                        dayjs(shift.duration.start).startOf('minute'),
                        dayjs(shift.duration.end).startOf('minute')
                    ],
                    participants: shift.participants.map(user => user.name)
                });
            }
        }
        if (mode === "ADD_SHIFT") {
            form.setFieldsValue({
                formName: "ADD_SHIFT",
                tomorrow: dayjs().add(1, 'day').startOf('day')
            });
        }
    }, [findShiftById, form, id, mode, props]);

    return (
        <>
            <Divider/>
            <Form
                form={form}
                onFinish={onSubmit}
                layout="vertical"
                style={{maxWidth: 600}}
            >
                <Form.Item
                    name="tomorrow"
                    label="Datum"
                    labelCol={{span: 24}}
                    wrapperCol={{span: 24}}
                >
                    <DatePicker
                        disabled
                        style={{width: '100%'}}
                    />
                </Form.Item>

                <Form.Item
                    name="duration"
                    label="Zeitraum"
                    rules={[
                        { required: true, message: "Zeitraum is required" },
                        {
                            validator: (_, value) => {
                                if (!value || value.length !== 2) return Promise.resolve();

                                const startTime: Dayjs = value[0]
                                const endTime: Dayjs = value[1]

                                const overlapExists = props.shifts.some(existingShift =>
                                    startTime < dayjs(existingShift.duration.end) &&
                                    endTime > dayjs(existingShift.duration.start)
                                );

                                console.log(overlapExists)

                                return overlapExists
                                    ? Promise.reject(new Error("Der Zeitraum überschneidet sich mit einer bestehenden Schicht"))
                                    : Promise.resolve();
                            },
                        },
                    ]}
                    labelCol={{span: 24}}
                    wrapperCol={{span: 24}}
                >
                    <TimePicker.RangePicker
                        format="HH:mm"
                        hourStep={1}
                        minuteStep={15}
                        style={{width: '100%'}}
                    />
                </Form.Item>

                {mode === "EDIT_SHIFT" && (
                    <Form.Item
                        name="participants"
                        label="Helfer"
                        labelCol={{span: 24}}
                        wrapperCol={{span: 24}}
                    >
                        <Select
                            mode="tags"
                            style={{width: '100%'}}
                            placeholder="Namen eingeben"
                        />
                    </Form.Item>
                )}

                {mode === "EDIT_SHIFT" && (
                    <Form.Item name="id" hidden>
                        <Input type="hidden"/>
                    </Form.Item>
                )}

                <Form.Item name="formName" hidden>
                    <Input type="hidden"/>
                </Form.Item>
            </Form>
        </>
    );
}
