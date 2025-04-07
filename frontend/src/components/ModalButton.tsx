import {useState} from 'react';
import {Button, DatePicker, Form, Modal, TimePicker} from 'antd';
import {Shift} from "../types/types.ts";
import dayjs from "dayjs";
import {PlusOutlined} from "@ant-design/icons";


interface Values {
    tomorrow: dayjs.Dayjs
    duration: [dayjs.Dayjs, dayjs.Dayjs]
}

interface ModalButtonProps {
    buttonName?: string,
    modalTitle?: string,
    handleSave: (shiftToSave: Shift) => void
}

export default function ModalButton(props: Readonly<ModalButtonProps>) {
    const [form] = Form.useForm();
    const [open, setOpen] = useState(false);

    const onCreate = (values: Values) => {
        const shiftData: Shift = {
            id: "",
            startTime: values.tomorrow
                .hour(values.duration[0].hour())
                .minute(values.duration[0].minute())
                .toDate(),
            endTime: values.tomorrow
                .hour(values.duration[1].hour())
                .minute(values.duration[1].minute())
                .toDate(),
            participants: []
        };

        props.handleSave(shiftData)

        setOpen(false);
    };

    return (
        <>
            <Button
                icon={<PlusOutlined/>}
                iconPosition={"end"}
                type="text"
                onClick={() => setOpen(true)}
            >
                {props.buttonName}
            </Button>
            <Modal
                open={open}
                title={props.modalTitle}
                okText="Create"
                cancelText="Cancel"
                okButtonProps={{autoFocus: true, htmlType: 'submit'}}
                onCancel={() => setOpen(false)}
                destroyOnClose
                modalRender={(dom) => (
                    <Form
                        layout="vertical"
                        form={form}
                        name="form_in_modal"
                        clearOnDestroy
                        onFinish={(values) => onCreate(values)}
                    >
                        {dom}
                    </Form>
                )}
            >
                <Form.Item
                    name="duration"
                    label="Select Time Range"
                    rules={[{required: true, message: 'Please select a time range!'}]}
                >
                    <TimePicker.RangePicker
                        format={'HH:mm'}
                        hourStep={1}
                        minuteStep={15}
                    />
                </Form.Item>

                <Form.Item
                    label="Placeholder Event Date"
                    name="tomorrow"
                    initialValue={dayjs().add(1, 'day').startOf('day')}
                >
                    <DatePicker disabled />
                </Form.Item>
            </Modal>
        </>
    );
};