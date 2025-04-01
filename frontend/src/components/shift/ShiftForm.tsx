import {Button, Stack, TagsInput} from "@mantine/core";
import {DateTimePicker, DateValue} from '@mantine/dates';
import {useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {Shift} from "../../types/types.ts";
import dayjs from "dayjs";

interface AddShiftFormProps {
    handleSave: (shiftToSave: Shift) => void
    handleUpdate: (is: string, shiftToSave: Shift) => void
}

export default function ShiftForm(props: Readonly<AddShiftFormProps>) {

    const location = useLocation()
    const navigate = useNavigate();
    const shift: Shift = location.state?.shift;

    const [startTime, setStartTime] = useState<DateValue>(shift?.startTime || undefined);
    const [endTime, setEndTime] = useState<DateValue>(shift?.endTime || undefined);
    const [participants, setParticipants] = useState<string[]>(
        shift ? shift.participants.map((user) => user.name) : []
    );

    const handleSubmit = () => {
        if (!startTime || !endTime) return;

        const shiftData: Shift = {
            id: shift?.id || "",
            startTime: dayjs(startTime).toDate(),
            endTime: dayjs(endTime).toDate(),
            participants: participants.map((name) => ({ name })),
        };

        if (location.pathname.startsWith("/add")){
            props.handleSave(shiftData)
        } else {
            props.handleUpdate(shiftData.id, shiftData);
        }

        navigate("/");
    };

    return (
        <Stack>
            <DateTimePicker
                withAsterisk
                onChange={setStartTime}
                label="Beginn"
                placeholder="Pick date and time"
                value={startTime}
            />

            <DateTimePicker
                withAsterisk
                onChange={setEndTime}
                label="Ende"
                placeholder="Pick date and time"
                value={endTime}
            />

            <TagsInput
                label="Helfer"
                placeholder="Füge Helfer hinzu"
                value={participants}
                onChange={setParticipants}
            />

            <Button fullWidth color="green" onClick={handleSubmit}>
                Schicht speichern
            </Button>

            <Button fullWidth onClick={() => navigate("/")}>
                Abbruch
            </Button>
        </Stack>
    );
}