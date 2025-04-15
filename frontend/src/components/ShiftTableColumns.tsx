import { Button, Flex, Space, Tag, Typography } from 'antd';
import { PlusOutlined, UserOutlined } from '@ant-design/icons';
import { Shift } from '../types/types';

interface ShiftTableColumnsProps {
    setUserModalVisible: (visible: boolean) => void;
    setSelectedShift: (shift: Shift) => void;
}

export default function ShiftTableColumns(props: Readonly<ShiftTableColumnsProps>) {

    const Text = Typography;

    return [
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