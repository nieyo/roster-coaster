import {Button, Stack} from "@mantine/core";
import {DateTimePicker, DateValue} from '@mantine/dates';
import React, {useState} from "react";
import axios from 'axios';
import {useNavigate} from "react-router-dom";

interface AddShiftFormProps {
    handleUpdate: () => void
}

export default function AddShiftForm(props: Readonly<AddShiftFormProps>) {

    const navigate = useNavigate();

    const [startTime, setStartTime] = useState<DateValue>();
    const [endTime, setEndTime] = useState<DateValue>();

    // TODO refactor, use hook -> useShiftState
    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        try {
            const baseURL = "/api/shift";
            await axios.post(baseURL, {
                startTime: startTime,
                endTime: endTime,
                participants: []
            });
            props.handleUpdate()
            navigate('/'); // Navigate to the main page
        } catch (error) {
            console.error('Error adding shift:', error);
        }
    };

    const handleCancel = () => {
        navigate('/'); // Navigate back to the main page
    };

    return (
        <Stack>
            <DateTimePicker onChange={setStartTime}
                            label="Beginn" placeholder="Pick date and time"/>
            <DateTimePicker onChange={setEndTime}
                            label="Ende" placeholder="Pick date and time"/>

            <Button
                fullWidth
                color={"green"}
                onClick={handleSubmit}
            >
                Schicht erstellen
            </Button>

            <Button
                fullWidth
                onClick={handleCancel}
            >
                Abbruch
            </Button>
        </Stack>
    )
}