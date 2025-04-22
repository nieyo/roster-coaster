import {DatePicker, Divider, Flex, Form, FormInstance, Input, Select, TimePicker} from "antd";
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
                    eventDate: dayjs(shift.duration.start).startOf("day"),
                    duration: [
                        dayjs(shift.duration.start).startOf("minute"),
                        dayjs(shift.duration.end).startOf("minute")
                    ],
                    participants: shift.participants.map(user => user.name),
                    min: shift.minParticipants,
                    max: shift.maxParticipants
                });
            }
        }
        if (mode === "ADD_SHIFT") {
            form.setFieldsValue({
                formName: "ADD_SHIFT",
                eventDate: dayjs().add(1, "day").startOf("day")
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
                    name="eventDate"
                    label="Datum"
                    labelCol={{span: 24}}
                    wrapperCol={{span: 24}}
                >
                    <DatePicker
                        disabled
                        style={{width: "100%"}}
                    />
                </Form.Item>

                <Form.Item
                    name="duration"
                    label="Zeitraum"
                    rules={[
                        {required: true, message: "Kein gültiger Zeitraum"},
                        {
                            validator: (_, value) => {
                                if (!value || value.length !== 2) return Promise.resolve();

                                const newShiftStart: Dayjs = form.getFieldValue("eventDate")
                                    .hour(value[0].hour())
                                    .minute(value[0].minute())
                                    .second(0)
                                    .millisecond(0);

                                const newShiftEnd: Dayjs = form.getFieldValue("eventDate")
                                    .hour(value[1].hour())
                                    .minute(value[1].minute())
                                    .second(0)
                                    .millisecond(0);

                                const now = dayjs();
                                if (newShiftStart.isBefore(now) || newShiftEnd.isBefore(now)) {
                                    return Promise.reject(new Error("Der Zeitraum darf nicht in der Vergangenheit liegen"));
                                }

                                const currentId: string | undefined = form.getFieldValue("id");

                                const overlapExists = props.shifts.some(existingShift => {
                                    if (currentId && existingShift.id === currentId) return false;
                                    return newShiftStart.isBefore(dayjs(existingShift.duration.end)) &&
                                        newShiftEnd.isAfter(dayjs(existingShift.duration.start));
                                });

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
                        style={{width: "100%"}}
                    />
                </Form.Item>

                {mode === "EDIT_SHIFT" && (
                    <Form.Item
                        name="participants"
                        label="Helfer"
                        extra={"Jeder Name kann nur einmal eingegeben werden."}
                        labelCol={{span: 24}}
                        wrapperCol={{span: 24}}
                    >
                        <Select
                            open={false}
                            mode="tags"
                            style={{width: "100%"}}
                            placeholder="Namen eingeben"
                        />
                    </Form.Item>
                )}

                <Flex gap="middle" style={{ width: '100%' }}>
                    <Form.Item
                        name="min"
                        label="Minimum Anzahl Helfer"
                        style={{ width: '100%' }}
                        rules={[
                            {
                                type: 'number',
                                min: 0,
                                message: 'Minimum muss größer oder gleich 0 sein',
                                transform: (value) => (value ? Number(value) : undefined),
                            },
                        ]}
                    >
                        <Input type="number" />
                    </Form.Item>

                    <Form.Item
                        name="max"
                        label="Maximum Anzahl Helfer"
                        style={{ width: '100%' }}
                        dependencies={['min']}
                        rules={[
                            {
                                type: 'number',
                                min: 0,
                                message: 'Maximum muss größer oder gleich 0 sein',
                                transform: (value) => (value ? Number(value) : undefined),
                            },
                            ({ getFieldValue }) => ({
                                validator(_, value) {
                                    const min = getFieldValue('min');
                                    if (
                                        value === undefined || value === '' ||
                                        min === undefined || min === ''
                                    ) {
                                        // Wenn eines der Felder leer ist, Validierung überspringen
                                        return Promise.resolve();
                                    }
                                    if (Number(value) < Number(min)) {
                                        return Promise.reject(new Error('Maximum darf nicht kleiner als Minimum sein'));
                                    }
                                    return Promise.resolve();
                                },
                            }),
                        ]}
                    >
                        <Input type="number" />
                    </Form.Item>
                </Flex>


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
