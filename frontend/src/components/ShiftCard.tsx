import React, {JSX} from 'react';
import {Card, Space, Button, Table, Popconfirm} from 'antd';
import {PlusOutlined, EditOutlined, DeleteOutlined, QuestionCircleOutlined} from '@ant-design/icons';
import {Shift} from '../types/types';
import {TableRowSelection} from "antd/es/table/interface";

interface ShiftTableCardProps {
    selectedRowKeys: React.Key[];
    setShiftModalVisible: (visible: boolean) => void;
    setEditShiftModalVisible: (visible: boolean) => void;
    handleDeleteShifts: (ids: string[]) => void;
    rowSelection: TableRowSelection<Shift>;
    columns: ({
        title: string
        width: number
        render: (record: Shift) => JSX.Element
    } | {
        title: string
        render: (record: Shift) => JSX.Element
        width: undefined
    })[];
    data: Shift[];
}

export default function ShiftCard(props: Readonly<ShiftTableCardProps>) {
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
                                props.handleDeleteShifts(props.selectedRowKeys.map(key => key.toString()))
                            }}
                            okText="Löschen"
                            cancelText="Abbrechen"
                            icon={<QuestionCircleOutlined style={{ color: 'red' }} />}
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
            title="Essensausgabe"
            variant="outlined"
        >
            <Table<Shift>
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
