import { DatePicker, Divider, Form, FormInstance, Input, Select, TimePicker } from "antd";
import { Shift, ShiftFormValues } from "../types/types.ts";
import dayjs from "dayjs";
import React, { useEffect } from "react";

interface ShiftFormProps {
    mode: string;
    onSubmit: (values: ShiftFormValues) => void;
    form: FormInstance;
    findShiftById?: (id: string) => Shift | undefined;
    id: React.Key[] | undefined;
}

export default function ShiftForm(props: Readonly<ShiftFormProps>) {
    const { mode, form, onSubmit, findShiftById, id } = props;

    useEffect(() => {
        if (mode === "EDIT_SHIFT" && id && findShiftById) {
            const shift = findShiftById(id.toString());
            if (shift) {
                form.setFieldsValue({
                    formName: "EDIT_SHIFT",
                    id: shift.id,
                    tomorrow: dayjs(shift.startTime).startOf('day'),
                    duration: [
                        dayjs(shift.startTime).startOf('minute'),
                        dayjs(shift.endTime).startOf('minute')
                    ],
                    participants: shift.participants.map(user => user.name)
                });
            }
        }
        if (mode === "ADD_SHIFT"){
            form.setFieldsValue({
                formName: "ADD_SHIFT",
                tomorrow: dayjs().add(1, 'day').startOf('day')
            });
        }
    }, [findShiftById, form, id, mode, props]);

    return (
        <>
            <Divider />
            <Form
                form={form}
                onFinish={onSubmit}
                layout="vertical"
                style={{ maxWidth: 600 }}
            >
                <Form.Item
                    name="tomorrow"
                    label="Datum"
                    labelCol={{ span: 24 }}
                    wrapperCol={{ span: 24 }}
                >
                    <DatePicker
                        disabled
                        style={{ width: '100%' }}
                    />
                </Form.Item>

                <Form.Item
                    name="duration"
                    label="Zeitraum"
                    rules={[{ required: true }]}
                    labelCol={{ span: 24 }}
                    wrapperCol={{ span: 24 }}
                >
                    <TimePicker.RangePicker
                        format="HH:mm"
                        hourStep={1}
                        minuteStep={15}
                        style={{ width: '100%' }}
                    />
                </Form.Item>

                {mode === "EDIT_SHIFT" && (
                    <Form.Item
                        name="participants"
                        label="Helfer"
                        labelCol={{ span: 24 }}
                        wrapperCol={{ span: 24 }}
                    >
                        <Select
                            mode="tags"
                            style={{ width: '100%' }}
                            placeholder="Namen eingeben"
                        />
                    </Form.Item>
                )}

                {mode === "EDIT_SHIFT" && (
                    <Form.Item name="id" hidden>
                        <Input type="hidden" />
                    </Form.Item>
                )}

                <Form.Item name="formName" hidden>
                    <Input type="hidden" />
                </Form.Item>
            </Form>
        </>
    );
}
