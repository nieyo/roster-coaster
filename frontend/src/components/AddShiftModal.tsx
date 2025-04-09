import {Modal, FormInstance} from "antd";
import AddShiftForm from "./AddShiftForm";
import { ShiftFormValues } from "../types/types";

interface AddShiftModalProps {
    visible: boolean;
    onCancel: () => void;
    onSubmit: (values: ShiftFormValues) => void;
    form: FormInstance; // Verwende den korrekten Form-Typ, falls vorhanden
}

export default function AddShiftModal(props: Readonly<AddShiftModalProps>) {
    return (
        <Modal
            title="Neue Schicht"
            open={props.visible}
            onOk={() => props.form.submit()}
            onCancel={props.onCancel}
            okText="Speichern"
            cancelText="Abbrechen"
            destroyOnClose
        >
            <AddShiftForm onSubmit={props.onSubmit} form={props.form} />
        </Modal>
    );
};