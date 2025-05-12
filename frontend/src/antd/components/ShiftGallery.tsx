import {Flex, Form, TableProps, Tag} from "antd";
import React, {useState} from "react";
import AddUserModal from "./AddUserModal.tsx";
import ShiftTableColumns from "./ShiftTableColumns.tsx";
import ShiftCard from "./ShiftCard.tsx";
import EventCard from "./EventCard.tsx";
import {useShiftGalleryHandlers} from "./shiftGallery.utils.ts";
import ShiftModal from "./ShiftModal.tsx";
import {CreateShiftDTO} from "@/antd/types/dto/CreateShiftDTO.ts";
import {ShiftDTO} from "@/antd/types/dto/ShiftDTO.ts";

type TableRowSelection<T extends object = object> = TableProps<T>["rowSelection"];

interface ShiftGalleryProps {
    shifts: ShiftDTO[],
    handleDelete: (id: string) => void,
    handleUpdate: (id: string, shiftToUpdate: ShiftDTO) => void,
    handleSave: (shiftToCreate: CreateShiftDTO) => void,
    toggleTheme: () => void,
    isDarkMode: boolean
}

export default function ShiftGallery(props: Readonly<ShiftGalleryProps>) {

    const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
    const [selectedShift, setSelectedShift] = useState<ShiftDTO>();

    const [form] = Form.useForm();

    const [shiftModalVisible, setShiftModalVisible] = useState(false);
    const [userModalVisible, setUserModalVisible] = useState(false);
    const [editShiftModalVisible, setEditShiftModalVisible] = useState(false);

    const {
        findShiftById,
        handleSubmit,
        handleDeleteShifts,
        handleClose
    } = useShiftGalleryHandlers({
        shifts: props.shifts,
        handleDelete: props.handleDelete,
        handleUpdate: props.handleUpdate,
        handleSave: props.handleSave,
        setSelectedRowKeys,
        setShiftModalVisible,
        setUserModalVisible,
        setEditShiftModalVisible,
        setSelectedShift,
        form
    });

    const data: ShiftDTO[] = props.shifts;
    const columns = ShiftTableColumns({setUserModalVisible, setSelectedShift});

    const onSelectChange = (newSelectedRowKeys: React.Key[]) => {
        setSelectedRowKeys(newSelectedRowKeys);
    };

    const rowSelection: TableRowSelection<ShiftDTO> = {
        selectedRowKeys,
        onChange: onSelectChange,
    };

    return (
        <>
            <ShiftModal
                visible={shiftModalVisible}
                onCancel={handleClose}
                onSubmit={handleSubmit}
                form={form}
                mode={"ADD_SHIFT"}
                shifts={props.shifts}
            />

            <ShiftModal
                visible={editShiftModalVisible}
                onCancel={handleClose}
                onSubmit={handleSubmit}
                form={form}
                findShiftById={findShiftById}
                id={selectedRowKeys}
                mode={"EDIT_SHIFT"}
                shifts={props.shifts}
            />

            <AddUserModal
                visible={userModalVisible}
                onCancel={handleClose}
                onSubmit={handleSubmit}
                form={form}
                selectedShift={selectedShift}
            />

            <Flex gap={20} vertical>
                <EventCard isDarkMode={props.isDarkMode} toggleTheme={props.toggleTheme}/>
                <Flex
                    justify={"center"}
                >
                    <Tag bordered color={"red"}>{"Helfer benötigt"}</Tag>
                    <Tag bordered color={"green"}>{"Ausreichend"}</Tag>
                    <Tag bordered color={"green-inverse"}>{"Maximum"}</Tag>
                </Flex>
                <ShiftCard
                    selectedRowKeys={selectedRowKeys}
                    setShiftModalVisible={setShiftModalVisible}
                    setEditShiftModalVisible={setEditShiftModalVisible}
                    handleDeleteShifts={handleDeleteShifts}
                    rowSelection={rowSelection}
                    columns={columns}
                    data={data}
                />
            </Flex>
        </>
    );
}