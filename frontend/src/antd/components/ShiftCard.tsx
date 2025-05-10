import React, {JSX} from "react";
import {Card, Space, Button, Table, Popconfirm, Typography} from "antd";
import {PlusOutlined, EditOutlined, DeleteOutlined, QuestionCircleOutlined} from "@ant-design/icons";
import {TableRowSelection} from "antd/es/table/interface";
import {ShiftDTO} from "@/antd/types/dto/ShiftDTO.ts";

interface ShiftTableCardProps {
    selectedRowKeys: React.Key[];
    setShiftModalVisible: (visible: boolean) => void;
    setEditShiftModalVisible: (visible: boolean) => void;
    handleDeleteShifts: (ids: string[]) => void;
    rowSelection: TableRowSelection<ShiftDTO>;
    columns: ({
        title: string
        width: number
        render: (record: ShiftDTO) => JSX.Element
    } | {
        title: string
        render: (record: ShiftDTO) => JSX.Element
        width: undefined
    })[];
    data: ShiftDTO[];
}

export default function ShiftCard(props: Readonly<ShiftTableCardProps>) {

    const Text = Typography;

    return (
        <Card
            extra={
                <Space.Compact>
                    {props.selectedRowKeys.length === 0 && (
                        <Button
                            icon={<PlusOutlined/>}
                            onClick={() => props.setShiftModalVisible(true)}
                            iconPosition="end"
                            type="default"
                            variant="filled"
                        >
                            Neue Schicht
                        </Button>
                    )}

                    {props.selectedRowKeys.length === 1 && (
                        <Button
                            type="default"
                            variant="outlined"
                            icon={<EditOutlined/>}
                            iconPosition="end"
                            onClick={() => props.setEditShiftModalVisible(true)}
                        >
                            Bearbeiten
                        </Button>
                    )}

                    {props.selectedRowKeys.length > 0 && (
                        <Popconfirm
                            title="Auswahl löschen?"
                            // description="Sind Sie sicher?"
                            onConfirm={() => {
                                props.handleDeleteShifts(props.selectedRowKeys.map(key => key.toString()));
                            }}
                            okText="Löschen"
                            cancelText="Abbrechen"
                            icon={<QuestionCircleOutlined style={{color: "red"}}/>}
                        >
                            <Button
                                type="text"
                                variant="outlined"
                                color="red"
                                icon={<DeleteOutlined/>}
                                iconPosition="end"
                            >
                                Löschen
                            </Button>
                        </Popconfirm>
                    )}
                </Space.Compact>
            }
            title={<Text>Essensausgabe</Text>}
            variant="outlined"
        >
            <Table<ShiftDTO>
                rowSelection={props.rowSelection}
                rowKey={(record) => record.id}
                size="small"
                bordered={true}
                pagination={false}
                columns={props.columns}
                dataSource={props.data}
            />
        </Card>
    );
}
