import { Button, Flex, Space, Tag, Typography } from 'antd';
import { PlusOutlined, UserOutlined } from '@ant-design/icons';
import {ShiftDTO} from "../types/dto/ShiftDTO.ts";
import dayjs from "dayjs";

interface ShiftTableColumnsProps {
    setUserModalVisible: (visible: boolean) => void;
    setSelectedShift: (shift: ShiftDTO) => void;
}

export default function ShiftTableColumns(props: Readonly<ShiftTableColumnsProps>) {

    const Text = Typography;

    return [
        {
            title: 'Zeitraum',
            width: 100,
            defaultSortOrder: 'ascend',
            sorter: (a: ShiftDTO, b: ShiftDTO) => dayjs(a.duration.start).valueOf() - dayjs(b.duration.start).valueOf(),
            sortDirections: ['ascend', 'descend', 'ascend'],
            showSorterTooltip: false,
            render: (record: ShiftDTO) => (
                <Text>
                    {dayjs(record.duration.start).format('HH:mm')} -{' '}
                    {dayjs(record.duration.end).format('HH:mm')}
                </Text>
            )
        },
        {
            title: 'Helfer',
            render: (record: ShiftDTO) => (
                <Flex justify="space-between" align="center">
                    <Space align="center" wrap>
                        {record.participants.length === 0 ? (
                            <Tag color="red" bordered={false}>
                                {record.participants.length}
                            </Tag>
                        ) : (
                            <Tag color="green">
                                {record.participants.length}
                            </Tag>
                        )}
                        {record.participants.length > 0 &&
                            record.participants.map((user) => (
                                <Space key={user.name}>
                                    <Tag key={user.name} bordered={false} icon={<UserOutlined />}>
                                        {user.name}
                                    </Tag>
                                </Space>
                            ))}
                    </Space>

                    <Button
                        size={'small'}
                        variant={'dashed'}
                        color="green"
                        icon={<PlusOutlined />}
                        onClick={() => {
                            props.setUserModalVisible(true);
                            props.setSelectedShift(record);
                        }}
                    >
                        Hier eintragen
                    </Button>
                </Flex>
            )
        }
    ];
};