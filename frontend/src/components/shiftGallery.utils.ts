// shiftGallery.utils.ts

import { Shift, ShiftFormValues, User } from "../types/types";
import React from "react";
import {FormInstance} from "antd";

interface HandleFunctionsProps {
    shifts: Shift[];
    handleDelete: (id: string) => void;
    handleUpdate: (id: string, shiftToSave: Shift) => void;
    handleSave: (shiftToSave: Shift) => void;
    setSelectedRowKeys: (keys: React.Key[]) => void;
    setShiftModalVisible: (visible: boolean) => void;
    setUserModalVisible: (visible: boolean) => void;
    setEditShiftModalVisible: (visible: boolean) => void;
    setSelectedShift: (shift: Shift | undefined) => void;
    form: FormInstance;
}

export const useShiftGalleryHandlers = (props: HandleFunctionsProps) => {

    const findShiftById = (id: string) => {
        return props.shifts.find(shift => shift.id === id);
    };

    const handleSubmit = (values: ShiftFormValues) => {
        console.log("Schichtdaten:", values);
        const formName = values.formName;
        console.log(formName);

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
        if (!values?.tomorrow || !values.duration?.length || values.duration.length < 2) {
            return;
        }

        const startTime = values.tomorrow.hour(values.duration[0].hour()).minute(values.duration[0].minute()).second(0).millisecond(0).toDate();
        const endTime = values.tomorrow.hour(values.duration[1].hour()).minute(values.duration[1].minute()).second(0).millisecond(0).toDate();

        const newShift: Shift = {
            id: "",
            startTime: startTime,
            endTime: endTime,
            participants: []
        };

        try {
            props.handleSave(newShift);
        } catch (error) {
            console.error("Fehler beim Speichern der Schicht:", error);
        }
    };

    const handleEditShift = (values: ShiftFormValues) => {
        if (!values?.tomorrow || !values.duration?.length || values.duration.length < 2 || !values?.participants || !values?.id) {
            return;
        }

        const id = values.id;
        const startTime = values.tomorrow.hour(values.duration[0].hour()).minute(values.duration[0].minute()).second(0).millisecond(0).toDate();
        const endTime = values.tomorrow.hour(values.duration[1].hour()).minute(values.duration[1].minute()).second(0).millisecond(0).toDate();
        const participants = values.participants.map(name => ({
            name: name,
        }));

        const newShift: Shift = {
            id: id,
            startTime: startTime,
            endTime: endTime,
            participants: participants
        };

        try {
            props.handleUpdate(values.id, newShift);
        } catch (error) {
            console.error("Fehler beim Update der Schicht:", error);
        }
    };

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
            props.handleUpdate(values.selectedShift.id, updatedShift);
        } catch (error) {
            console.error("Fehler beim Update der Schicht:", error);
        }
    };

    const handleDeleteShifts = () => {
        if (props.shifts.length === 0) {
            return;
        }

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
            tomorrow: undefined,
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
