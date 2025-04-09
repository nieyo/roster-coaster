import {Modal, FormInstance} from "antd";
import AddUserForm from "./AddUserForm";
import { Shift, ShiftFormValues } from "../types/types";

interface AddUserModalProps {
    visible: boolean;
    onCancel: () => void;
    onSubmit: (values: ShiftFormValues) => void;
    form: FormInstance; // Verwende den korrekten Form-Typ, falls vorhanden
    selectedShift?: Shift;
}

export default function AddUserModal(props: Readonly<AddUserModalProps>) {
    return (
        <Modal
            title="Für Schicht eintragen"
            open={props.visible}
            onOk={() => props.form.submit()}
            onCancel={props.onCancel}
            okText="Speichern"
            cancelText="Abbrechen"
            destroyOnClose
        >
            <AddUserForm onSubmit={props.onSubmit} form={props.form} selectedShift={props.selectedShift} />
        </Modal>
    );
};
