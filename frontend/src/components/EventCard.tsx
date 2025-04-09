import { Card, Space, Button } from 'antd';
import { SunOutlined, MoonOutlined } from '@ant-design/icons';

interface EventCardProps {
    isDarkMode: boolean;
    toggleTheme: () => void;
}

export default function EventCard(props: Readonly<EventCardProps>) {
    return (
        <Card
            title="Event"
            extra={
                <Space>
                    <Space.Compact>
                        <Button onClick={() => {}}>Add Task</Button>
                        <Button onClick={() => {}}>Delete Task</Button>
                        <Button onClick={() => {}}>Update Task</Button>
                    </Space.Compact>
                    <Button icon={props.isDarkMode ? <SunOutlined /> : <MoonOutlined />} onClick={props.toggleTheme} />
                </Space>
            }
        />
    );
};