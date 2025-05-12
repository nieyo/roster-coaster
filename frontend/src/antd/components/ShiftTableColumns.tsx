import {Button, Flex, Space, Tag, Typography} from "antd";
import {PlusOutlined, UserOutlined} from "@ant-design/icons";
import {ShiftDTO} from "@/antd/types/dto/ShiftDTO.ts";
import dayjs from "dayjs";

interface ShiftTableColumnsProps {
    setUserModalVisible: (visible: boolean) => void;
    setSelectedShift: (shift: ShiftDTO) => void;
}

function getTagColor(length: number, min: number, max: number): string {
    if (min === 0 && max === 0) return "green";
    if (min > 0 && max === 0) return length < min ? "red" : "green";
    if (min === 0 && max > 0) return length < max ? "red" : "green-inverse";
    if (length < min) return "red";
    if (length < max) return "green";
    return "green-inverse";
}

function getTagText(length: number, min: number, max: number): string | number {
    if (min === 0 && max === 0) return length;
    if (min > 0 && max === 0) return `${length} / ${min}`;
    if (max > 0) return `${length} / ${max}`;
    return length;
}

export default function ShiftTableColumns(props: Readonly<ShiftTableColumnsProps>) {

    const Text = Typography;

    return [
        {
            title: "Zeitraum",
            width: 110,
            defaultSortOrder: "ascend",
            sorter: (a: ShiftDTO, b: ShiftDTO) => dayjs(a.duration.start).valueOf() - dayjs(b.duration.start).valueOf(),
            sortDirections: ["ascend", "descend", "ascend"],
            showSorterTooltip: false,
            render: (record: ShiftDTO) => (
                <Text>
                    {dayjs(record.duration.start).format("HH:mm")} -{" "}
                    {dayjs(record.duration.end).format("HH:mm")}
                </Text>
            )
        },
        {
            title: "Helfer",
            render: (record: ShiftDTO) => {

                const length = record.signups.length;
                const min = record.minParticipants;
                const max = record.maxParticipants;

                const tagColor = getTagColor(length, min, max);
                const tagText = getTagText(length, min, max);

                return (
                    <Flex justify="space-between" align="center">
                        <Space align="center" wrap>
                            <Tag color={tagColor} bordered>
                                {tagText}
                            </Tag>


                            {record.signups.length > 0 &&
                                record.signups.map((user) => (
                                    <Space key={user.name}>
                                        <Tag key={user.name} bordered={false} icon={<UserOutlined/>}>
                                            {user.name}
                                        </Tag>
                                    </Space>
                                ))}
                        </Space>


                        <Button
                            disabled={
                                !dayjs(record.duration.end).isAfter(dayjs())
                                || (
                                    record.maxParticipants === record.signups.length
                                    && record.maxParticipants > 0
                                )
                            }
                            size={"small"}
                            variant={"dashed"}
                            color="green"
                            icon={<PlusOutlined/>}
                            onClick={() => {
                                props.setUserModalVisible(true);
                                props.setSelectedShift(record);
                            }}
                        >
                            Hier eintragen
                        </Button>

                    </Flex>
                );
            }
        }
    ];
};