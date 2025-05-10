import {Modal, FormInstance} from "antd";
import AddUserForm from "./AddUserForm.tsx";
import {ShiftFormValues} from "@/antd/types/form/ShiftFormValues.ts";
import {ShiftDTO} from "@/antd/types/dto/ShiftDTO.ts";

interface AddUserModalProps {
    visible: boolean;
    onCancel: () => void;
    onSubmit: (values: ShiftFormValues) => void;
    form: FormInstance;
    selectedShift?: ShiftDTO;
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
