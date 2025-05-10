import {Modal, FormInstance} from "antd";
import React from "react";
import {ShiftFormValues} from "@/antd/types/form/ShiftFormValues.ts";
import ShiftForm from "./ShiftForm.tsx";
import {ShiftDTO} from "@/antd/types/dto/ShiftDTO.ts";

interface GenericShiftModalProps {
    mode: "ADD_SHIFT" | "EDIT_SHIFT",
    visible: boolean,
    onCancel: () => void,
    onSubmit: (values: ShiftFormValues) => void,
    form: FormInstance,
    findShiftById?: (id: string) => ShiftDTO | undefined,
    id?: React.Key[],
    shifts: ShiftDTO[]
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
