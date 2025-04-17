import {Divider, Form, FormInstance, Input} from "antd";
import {useEffect} from "react";
import {ShiftDTO} from "../types/dto/ShiftDTO.ts";
import {ShiftFormValues} from "../types/form/ShiftFormValues.ts";

interface AddUserFormProps {
    onSubmit: (values: ShiftFormValues) => void,
    form: FormInstance,
    selectedShift?: ShiftDTO
}

export default function AddUserForm(props: Readonly<AddUserFormProps>) {

    useEffect(() => {
        props.form.setFieldsValue({
            selectedShift: props.selectedShift,
            formName: "ADD_USER"
        });
    }, [props.selectedShift, props.form]);

    return (
        <>
            <Divider/>
            <Form
                form={props.form}
                onFinish={props.onSubmit}
                layout="vertical"
                style={{maxWidth: 600}}
            >
                <Form.Item
                    name="name"
                    label="Name"
                    labelCol={{span: 24}}
                    wrapperCol={{span: 24}}
                >
                    <Input
                        style={{width: '100%'}}
                    />
                </Form.Item>
                <Form.Item name="selectedShift" hidden>
                    <Input type="hidden" />
                </Form.Item>
                <Form.Item name="formName" hidden>
                    <Input type="hidden" />
                </Form.Item>
            </Form>
        </>
    )
}