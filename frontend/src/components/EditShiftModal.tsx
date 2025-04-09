import React from "react";
import {Modal, FormInstance} from "antd";
import EditShiftForm from "./EditShiftForm";
import { Shift, ShiftFormValues } from "../types/types";

interface EditShiftModalProps {
    visible: boolean;
    onCancel: () => void;
    onSubmit: (values: ShiftFormValues) => void;
    form: FormInstance;
    findShiftById: (id: string) => Shift | undefined;
    id: React.Key[];
}

export default function EditShiftModal(props: Readonly<EditShiftModalProps>) {
    return (
        <Modal
            title="Schicht bearbeiten"
            open={props.visible}
            onOk={() => props.form.submit()}
            onCancel={props.onCancel}
            okText="Speichern"
            cancelText="Abbrechen"
            destroyOnClose
        >
            <EditShiftForm onSubmit={props.onSubmit} form={props.form} findShiftById={props.findShiftById} id={props.id} />
        </Modal>
    );
};