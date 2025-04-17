import React from "react";
import {FormInstance} from "antd";
import {CreateShiftDTO} from "../types/dto/CreateShiftDTO.ts";
import {ShiftFormValues} from "../types/form/ShiftFormValues.ts";
import {ShiftDTO} from "../types/dto/ShiftDTO.ts";
import {UserDTO} from "../types/dto/UserDTO.ts";
import {Dayjs} from "dayjs";

interface HandleFunctionsProps {
    shifts: ShiftDTO[];
    handleDelete: (id: string) => void;
    handleUpdate: (id: string, shiftToUpdate: ShiftDTO) => void;
    handleSave: (shiftToCreate: CreateShiftDTO) => void;
    setSelectedRowKeys: (keys: React.Key[]) => void;
    setShiftModalVisible: (visible: boolean) => void;
    setUserModalVisible: (visible: boolean) => void;
    setEditShiftModalVisible: (visible: boolean) => void;
    setSelectedShift: (shift: ShiftDTO | undefined) => void;
    form: FormInstance;
}

export const useShiftGalleryHandlers = (props: HandleFunctionsProps) => {

    const findShiftById = (id: string) => {
        return props.shifts.find(shift => shift.id === id);
    };

    const handleSubmit = (values: ShiftFormValues) => {
        console.log("Schichtdaten:", values);
        const formName = values.formName;

        if (formName === "ADD_SHIFT") {
            handleAddShift(values);
        }

        if (formName === "ADD_USER") {
            handleAddUser(values);
        }

        if (formName === "EDIT_SHIFT") {
            handleEditShift(values);
        }

        handleClose();
    };

    const handleAddShift = (values: ShiftFormValues) => {
        if (!values?.eventDate || !values.duration?.length || values.duration.length < 2) {
            return;
        }

        const createShiftDTO: CreateShiftDTO = {
            duration: {
                start: mergeTime(values.eventDate, values.duration[0]),
                end: mergeTime(values.eventDate, values.duration[1])
            },
            participants: []
        };

        try {
            props.handleSave(createShiftDTO);
        } catch (error) {
            console.error("Fehler beim Speichern der Schicht:", error);
        }
    };

    function mergeTime(date: Dayjs, time: Dayjs): string {
        return date
            .hour(time.hour())
            .minute(time.minute())
            .second(time.second())
            .millisecond(time.millisecond())
            .toISOString();
    }

    const handleEditShift = (values: ShiftFormValues) => {
        if (!values?.eventDate || !values.duration?.length || values.duration.length < 2 || !values?.participants || !values?.id) {
            return;
        }

        const shiftToUpdate: ShiftDTO = {
            id: values.id,
            duration: {
                start: mergeTime(values.eventDate, values.duration[0]),
                end: mergeTime(values.eventDate, values.duration[1])
            },
            participants: values.participants.map(name => ({
                name: name,
            }))
        };

        try {
            props.handleUpdate(values.id, shiftToUpdate);
        } catch (error) {
            console.error("Fehler beim Update der Schicht:", error);
        }
        props.setSelectedRowKeys([]);
    };

    const handleAddUser = (values: ShiftFormValues) => {
        if (!values?.selectedShift || !values.name) {
            return;
        }

        const newParticipant: UserDTO = {
            name: values.name
        };

        const updatedShift: ShiftDTO = {
            ...values.selectedShift,
            participants: [
                ...values.selectedShift.participants,
                newParticipant
            ]
        };

        try {
            props.handleUpdate(values.selectedShift.id, updatedShift);
        } catch (error) {
            console.error("Fehler beim Update der Schicht:", error);
        }
    };

    const handleDeleteShifts = (ids: string[]) => {

        if (props.shifts.length === 0) {
            return;
        }

        ids.forEach((id) => {
            try {
                props.handleDelete(id.toString());
            } catch (error) {
                console.error("Fehler beim Löschen der Schicht:", error);
            }
        });
        props.setSelectedRowKeys([]);
    };

    const handleClose = () => {
        props.setShiftModalVisible(false);
        props.setUserModalVisible(false);
        props.setEditShiftModalVisible(false);
        props.setSelectedShift(undefined);

        props.form.setFieldsValue({
            formName: undefined,
            selectedShift: undefined,
            name: undefined,
            eventDate: undefined,
            duration: undefined,
            participants: [],
        });

        props.form.resetFields();
    };

    return {
        findShiftById,
        handleSubmit,
        handleAddShift,
        handleEditShift,
        handleAddUser,
        handleDeleteShifts,
        handleClose
    };
};
