import {Shift, ShiftFormValues} from "../types/types.ts";
import {DatePicker, Divider, Form, FormInstance, Select, SelectProps, TimePicker, Typography} from "antd";
import React from "react";
import dayjs from "dayjs";

interface EditShiftFormProps {
    onSubmit: (values: ShiftFormValues) => void,
    form: FormInstance,
    findShiftById: (id: string) => Shift | undefined,
    id: React.Key[]
}

export default function EditShiftForm(props: Readonly<EditShiftFormProps>) {

    const Text = Typography
    const options: SelectProps['options'] = [];
    const shiftToUpdate = props.findShiftById(props.id.toString())

    if (!shiftToUpdate) {
        return (
            <>
                <Divider/>
                <Text>Schicht nicht gefunden</Text>
            </>
        );
    }

    const initialValues = {
        formName: "EDIT_SHIFT",
        id: shiftToUpdate.id,
        tomorrow: dayjs(shiftToUpdate.startTime).startOf('day'),
        duration: [
            dayjs(shiftToUpdate.startTime).startOf('minute'),
            dayjs(shiftToUpdate.endTime).startOf('minute'),
        ],
        participants: shiftToUpdate.participants.map(user => user.name)
    };

    return (
        <>
            <Divider/>
            <Form
                name="editShiftForm"
                form={props.form}
                onFinish={props.onSubmit}
                layout="vertical"
                style={{maxWidth: 600}}

                initialValues={initialValues}
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
                        options={options}
                    />
                </Form.Item>
                <Form.Item name="id" noStyle />
                <Form.Item name="formName" noStyle />


            </Form>
        </>
    )
}