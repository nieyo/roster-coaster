import {Shift, ShiftFormValues} from "../types/types.ts";
import {DatePicker, Divider, Form, FormInstance, Input, Select, TimePicker} from "antd";
import React, {useEffect} from "react";
import dayjs from "dayjs";

interface EditShiftFormProps {
    onSubmit: (values: ShiftFormValues) => void,
    form: FormInstance,
    findShiftById: (id: string) => Shift | undefined,
    id: React.Key[]
}

export default function EditShiftForm(props: Readonly<EditShiftFormProps>) {

    useEffect(() => {
        const shift = props.findShiftById(props.id.toString());
        if (shift) {
            props.form.setFieldsValue({
                formName: "EDIT_SHIFT",
                id: shift.id,
                tomorrow: dayjs(shift.startTime).startOf('day'),
                duration: [
                    dayjs(shift.startTime).startOf('minute'),
                    dayjs(shift.endTime).startOf('minute')
                ],
                participants: shift.participants.map(u => u.name)
            });
        }
    }, [props]);

    return (
        <>
            <Divider/>
            <Form
                name="editShiftForm"
                form={props.form}
                onFinish={props.onSubmit}
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
                    rules={[{required: true}]}
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

                <Form.Item
                    name="participants"
                    label="Helfer"
                    labelCol={{span: 24}}
                    wrapperCol={{span: 24}}
                >
                    <Select
                        mode="tags"
                        style={{ width: '100%' }}
                        placeholder="Tags Mode"
                    />
                </Form.Item>
                <Form.Item name="id" hidden>
                    <Input type="hidden" />
                </Form.Item>
                <Form.Item name="formName" hidden>
                    <Input type="hidden" />
                </Form.Item>


            </Form>
        </>
    )
}