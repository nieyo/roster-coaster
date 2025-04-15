import {Modal, FormInstance} from "antd";
import ShiftForm from "./ShiftForm"; // Die zuvor erstellte kombinierte Form
import {Shift, ShiftFormValues} from "../types/types";
import React from "react";

interface GenericShiftModalProps {
    mode: "ADD_SHIFT" | "EDIT_SHIFT",
    visible: boolean,
    onCancel: () => void,
    onSubmit: (values: ShiftFormValues) => void,
    form: FormInstance,
    findShiftById?: (id: string) => Shift | undefined,
    id?: React.Key[],
    shifts: Shift[]
}

export default function ShiftModal(props: Readonly<GenericShiftModalProps>) {
    const modalConfig = {
        ADD_SHIFT: {
            title: "Neue Schicht",
            formMode: "ADD_SHIFT"
        },
        EDIT_SHIFT: {
            title: "Schicht bearbeiten",
            formMode: "EDIT_SHIFT"
        }
    };

    const currentConfig = modalConfig[props.mode];

    return (
        <Modal
            title={currentConfig.title}
            open={props.visible}
            onOk={() => props.form.submit()}
            onCancel={props.onCancel}
            okText="Speichern"
            cancelText="Abbrechen"
            destroyOnClose
        >
            <ShiftForm
                mode={currentConfig.formMode}
                onSubmit={props.onSubmit}
                form={props.form}
                shifts={props.shifts}

                findShiftById={props.mode === "EDIT_SHIFT" ? props.findShiftById : undefined}
                id={props.mode === "EDIT_SHIFT" ? props.id : undefined}

            />
        </Modal>
    );
}
