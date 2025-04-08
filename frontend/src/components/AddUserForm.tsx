import {Divider, Form, FormInstance, Input} from "antd";
import {Shift, ShiftFormValues} from "../types/types.ts";

interface AddUserFormProps {
    onSubmit: (values: ShiftFormValues) => void,
    form: FormInstance,
    selectedShift?: Shift
}

export default function AddUserForm(props: Readonly<AddUserFormProps>) {

    return (
        <>
            <Divider/>
            <Form
                form={props.form}
                onFinish={props.onSubmit}
                layout="vertical"
                style={{maxWidth: 600}}
                initialValues={{
                    formName: "ADD_USER",
                    selectedShift: props.selectedShift
                }}
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
                <Form.Item name="selectedShift" noStyle />
                <Form.Item name="formName" noStyle />
            </Form>
        </>
    )
}