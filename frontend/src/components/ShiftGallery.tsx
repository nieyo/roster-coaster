import {Shift, ShiftFormValues, User} from "../types/types.ts";
import {Button, Card, Flex, Form, Modal, Space, Table, TableProps, Tag, Typography} from "antd";
import {
    DeleteOutlined, EditOutlined,
    MoonOutlined,
    PlusOutlined,
    SunOutlined,
    UserOutlined
} from "@ant-design/icons";
import React, {useState} from "react";
import AddShiftForm from "./AddShiftForm.tsx";
import AddUserForm from "./AddUserForm.tsx";
import EditShiftForm from "./EditShiftForm.tsx";

type TableRowSelection<T extends object = object> = TableProps<T>['rowSelection'];

interface ShiftGalleryProps {
    shifts: Shift[],
    handleDelete: (id: string) => void,
    handleUpdate: (id: string, shiftToSave: Shift) => void,
    handleSave: (shiftToSave: Shift) => void,
    toggleTheme: () => void,
    isDarkMode: boolean
}

export default function ShiftGallery(props: Readonly<ShiftGalleryProps>) {

    const Text = Typography;

    const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
    const [selectedShift, setSelectedShift] = useState<Shift>();

    const [form] = Form.useForm();

    const [shiftModalVisible, setShiftModalVisible] = useState(false);
    const [userModalVisible, setUserModalVisible] = useState(false);
    const [editShiftModalVisible, setEditShiftModalVisible] = useState(false);

    const findShiftById = (id: string) => {
        return props.shifts.find(shift => shift.id === id)
    }

    const handleSubmit = (values: ShiftFormValues, ) => {

        console.log("Schichtdaten:", values);
        const formName = values.formName;
        console.log(formName)

        if (formName === "ADD_SHIFT") {
            handleAddShift(values)
        }

        if (formName === "ADD_USER") {
            handleAddUser(values)
        }

        if (formName === "EDIT_SHIFT") {
            handleEditShift(values)
        }

        handleClose()
    };

    const handleAddShift = (values: ShiftFormValues) => {
        if (!values?.tomorrow || !values.duration?.length || values.duration.length < 2) {
            return;
        }

        const startTime = values.tomorrow.hour(values.duration[0].hour()).minute(values.duration[0].minute()).second(0).millisecond(0).toDate()
        const endTime = values.tomorrow.hour(values.duration[1].hour()).minute(values.duration[1].minute()).second(0).millisecond(0).toDate()

        const newShift: Shift = {
            id: "",
            startTime: startTime,
            endTime: endTime,
            participants: []
        };

        try {
            props.handleSave(newShift)
        } catch (error) {
            console.error("Fehler beim Speichern der Schicht:", error);
        }
    }

    const handleEditShift = (values: ShiftFormValues) => {

        if (!values?.tomorrow || !values.duration?.length || values.duration.length < 2 || !values?.participants || !values?.id) {
            return;
        }

        const id = values.id
        const startTime = values.tomorrow.hour(values.duration[0].hour()).minute(values.duration[0].minute()).second(0).millisecond(0).toDate()
        const endTime = values.tomorrow.hour(values.duration[1].hour()).minute(values.duration[1].minute()).second(0).millisecond(0).toDate()
        const participants = values.participants.map(name => ({
            name: name,
        }))

        const newShift: Shift = {
            id: id,
            startTime: startTime,
            endTime: endTime,
            participants: participants
        };

        try {
            props.handleUpdate(values.id, newShift)
        } catch (error) {
            console.error("Fehler beim Update der Schicht:", error);
        }
    }

    const handleAddUser = (values: ShiftFormValues) => {

        if (!values?.selectedShift || !values.name) {
            return;
        }


        const newParticipant: User = {
            name: values.name
        };

        const updatedShift: Shift = {
            ...values.selectedShift,
            participants: [
                ...values.selectedShift.participants,
                newParticipant
            ]
        };

        try {
            props.handleUpdate(values.selectedShift.id, updatedShift)
        } catch (error) {
            console.error("Fehler beim Update der Schicht:", error);
        }
    }

    const handleDeleteShifts = () => {
        if (selectedRowKeys.length === 0) {
            return
        }

        selectedRowKeys.forEach((id) => {
            try {
                props.handleDelete(id.toString())
            } catch (error) {
                console.error("Fehler beim Löschen der Schicht:", error);
            }
            setSelectedRowKeys([])
        })
    }

    const handleClose = () => {
        setShiftModalVisible(false)
        setUserModalVisible(false)
        setEditShiftModalVisible(false)
        setSelectedShift(undefined)
        form.setFieldsValue({formName: undefined});
        form.resetFields()
    }

    const data: Shift[] = props.shifts
    const columns = [
        {
            title: 'Zeitraum',
            width: 100,
            render: (record: Shift) => (
                <Text>
                    {record.startTime.toLocaleTimeString('de-DE', {
                        hour: '2-digit',
                        minute: '2-digit'
                    })} -{' '}
                    {record.endTime.toLocaleTimeString('de-DE', {
                        hour: '2-digit',
                        minute: '2-digit'
                    })}
                </Text>
            )

        },
        {
            title: 'Teilnehmer',
            render: (record: Shift) => (
                <Flex justify="space-between" align="center">
                    <Space align="center">
                        {
                            record.participants.length === 0
                                ? <Tag color="red" bordered={false}>{record.participants.length}</Tag>
                                : <Tag>{record.participants.length}</Tag>
                        }
                        {
                            record.participants.length > 0 && record.participants.map((user) => (
                                <Space key={user.name}>
                                    <Tag
                                        key={user.name}
                                        bordered={false}
                                        icon={<UserOutlined/>}
                                    >
                                        {user.name}
                                    </Tag>
                                </Space>
                            ))
                        }
                    </Space>


                    <Button size={"small"} variant={"dashed"}
                            color="green"
                            icon={<PlusOutlined/>}
                            onClick={() => {
                                setUserModalVisible(true)
                                setSelectedShift(record)
                            }}
                    >
                        Hier eintragen
                    </Button>
                </Flex>
            )
        }
    ]

    const onSelectChange = (newSelectedRowKeys: React.Key[]) => {
        console.log('ShiftGallery Line: 183 --> selectedRowKeys changed: ', newSelectedRowKeys);
        setSelectedRowKeys(newSelectedRowKeys);
    };

    const rowSelection: TableRowSelection<Shift> = {
        selectedRowKeys,
        onChange: onSelectChange,
    };

    return (
        <>
            <Modal
                title="Neue Schicht"
                open={shiftModalVisible}
                onOk={() => form.submit()}
                onCancel={() => handleClose()}
                okText={"Speichern"}
                cancelText={"Abbrechen"}
                destroyOnClose
            >
                <AddShiftForm onSubmit={handleSubmit} form={form}/>
            </Modal>

            <Modal
                title="Für Schicht eintragen"
                open={userModalVisible}
                onOk={() => form.submit()}
                onCancel={() => handleClose()}
                okText={"Speichern"}
                cancelText={"Abbrechen"}
                destroyOnClose
            >
                <AddUserForm onSubmit={handleSubmit} form={form} selectedShift={selectedShift}/>
            </Modal>

            <Modal
                title="Schicht bearbeiten"
                open={editShiftModalVisible}
                onOk={() => form.submit()}
                onCancel={() => handleClose()}
                okText={"Speichern"}
                cancelText={"Abbrechen"}
                destroyOnClose
            >
                <EditShiftForm onSubmit={handleSubmit} form={form} findShiftById={findShiftById} id={selectedRowKeys} />
            </Modal>

            <Flex gap={20} vertical>
                <Card
                    title="Event"
                    extra={
                        <Space>
                            <Space.Compact>
                                <Button
                                    onClick={() => {
                                    }}>
                                    Add Task
                                </Button>
                                <Button
                                    onClick={() => {
                                    }}>
                                    Delete Task
                                </Button>
                                <Button
                                    onClick={() => {
                                    }}>
                                    Update Task
                                </Button>
                            </Space.Compact>
                            <Button icon={props.isDarkMode ? <SunOutlined/> : <MoonOutlined/>}
                                    onClick={() => props.toggleTheme()}/>
                        </Space>
                    }
                >
                </Card>

                <Card
                    extra={
                        <Space.Compact>
                            {
                                selectedRowKeys.length === 0 &&
                                <Button
                                    icon={<PlusOutlined/>}
                                    onClick={() => setShiftModalVisible(true)}
                                    iconPosition={"end"}
                                    type={"default"}
                                    variant={"filled"}
                                >
                                    Neue Schicht
                                </Button>
                            }

                            {
                                selectedRowKeys.length === 1 &&
                                <Button
                                    type={"default"}
                                    variant={"outlined"}
                                    icon={<EditOutlined />}
                                    iconPosition={"end"}
                                    onClick={() => {setEditShiftModalVisible(true)}}
                                >
                                    Bearbeiten
                                </Button>
                            }

                            {
                                selectedRowKeys.length > 0 &&
                                <Button
                                    type={"text"}
                                    variant={"outlined"}
                                    color={"red"}
                                    icon={<DeleteOutlined/>}
                                    iconPosition={"end"}
                                    onClick={() => {
                                        handleDeleteShifts()
                                    }}
                                >
                                    Löschen
                                </Button>
                            }
                        </Space.Compact>
                    }
                    title="Essensausgabe"
                    variant={"outlined"}
                >
                    <Table<Shift>
                        rowSelection={rowSelection}
                        rowKey={(record) => record.id}
                        size={"small"}
                        bordered={true}
                        pagination={false}
                        columns={columns}
                        dataSource={data}
                    />
                </Card>
            </Flex>
        </>
    )
}