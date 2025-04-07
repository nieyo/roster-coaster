import {Shift} from "../types/types.ts";
import {Card, Flex, Table, Tag, Typography} from "antd";
import {PlusOutlined, UserOutlined} from "@ant-design/icons";
import ModalButton from "./ModalButton.tsx";

interface ShiftGalleryProps {
    shifts: Shift[],
    handleDelete: (id: string) => void,
    handleUpdate: (id: string, shiftToSave: Shift) => void,
    handleSave: (shiftToSave: Shift) => void
}

export default function ShiftGallery(props: Readonly<ShiftGalleryProps>) {

    const Text = Typography;

    // const [name, setName] = useState<string>("");
    // TODO - fix handleAddParticipant
    // const handleAddParticipant = () => {
    //     if (name.trim() === "") return;
    //
    //     const newParticipant: User = {
    //         name: name,
    //     };
    //
    //     const updatedShift: Shift = {
    //         ...props.shift,
    //         participants: [...props.shift.participants, newParticipant]
    //     };
    //
    //     props.handleUpdate(props.shift.id, updatedShift);
    //
    //     setName("");
    // };

    const data: Shift[] = props.shifts
    const columns = [
        {
            title: 'Zeitraum',
            width: 125,
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
                <>
                    {
                        record.participants.length === 0
                            ? <Tag color="red" bordered={false}>{record.participants.length}</Tag>
                            : <Tag>{record.participants.length}</Tag>
                    }
                    {
                        record.participants.length > 0 && record.participants.map((user) => (
                            <Tag
                                key={user.name}
                                bordered={false}
                                icon={<UserOutlined/>}
                            >
                                {user.name}
                            </Tag>
                        ))
                    }

                    <Tag
                        color="green"
                        bordered={false}
                        icon={<PlusOutlined/>}
                        onClick={() => {
                            console.log("click")
                        }}
                    >
                        Hier eintragen
                    </Tag>
                </>
            )
        }
    ]

    return (
        <Flex gap={20} vertical>
            <Card title="Event">

            </Card>

            <Card
                extra={
                    <ModalButton
                        buttonName={"Neue Schicht"}
                        modalTitle={"Erstelle eine neue Schicht"}
                        handleSave={props.handleSave}
                    />
                }
                title="Essensausgabe"
                variant={"outlined"}
            >
                <Table<Shift>
                    rowKey={(record) => record.startTime.toUTCString()}
                    size={"small"}
                    bordered={true}
                    pagination={false}
                    columns={columns}
                    dataSource={data}
                />
            </Card>
        </Flex>
    )
}