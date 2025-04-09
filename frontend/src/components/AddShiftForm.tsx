import {DatePicker, Divider, Form, FormInstance, Input, TimePicker} from 'antd';
import {ShiftFormValues} from "../types/types.ts";
import dayjs from "dayjs";
import {useEffect} from "react";


interface AddShiftFormProps {
    onSubmit?: (values: ShiftFormValues) => void,
    form: FormInstance,
}

export default function AddShiftForm(props: Readonly<AddShiftFormProps>) {

    useEffect(() => {
        props.form.setFieldsValue({
            formName: "ADD_SHIFT"
        });
    }, [props.form]);

    return (
        <>
            <Divider/>
            <Form
                form={props.form}
                onFinish={props.onSubmit}
                layout="vertical"
                style={{maxWidth: 600}}

                initialValues={{
                    formName: "ADD_SHIFT",
                    tomorrow: dayjs().add(1, 'day').startOf('day')
                }}
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
                <Form.Item name="formName" hidden>
                    <Input type="hidden" />
                </Form.Item>
            </Form>
        </>

    );
};