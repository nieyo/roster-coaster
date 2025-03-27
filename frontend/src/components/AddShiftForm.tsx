import {Button, Center, Stack, TextInput} from "@mantine/core";
import {DateTimePicker, DateValue} from '@mantine/dates';
import React, {useState} from "react";
import axios from 'axios';
import {useNavigate} from "react-router-dom";

export default function AddShiftForm() {

    const navigate = useNavigate();

    const [name, setName] = useState('');
    const [startTime, setStartTime] = useState<DateValue>();
    const [endTime, setEndTime] = useState<DateValue>();

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        try {
            const baseURL = "/api/shift";
            await axios.post(baseURL, { //POST request
                name: name,
                startTime: startTime,
                endTime: endTime,
            });
            navigate('/shifts'); // Navigate back to the main page
        } catch (error) {
            console.error('Error adding shift:', error);
        }
    };

    const handleCancel = () => {
        navigate('/shift'); // Navigate back to the main page
    };

    return (
        <Center>
            <Stack>
                <TextInput
                    label="Name der Schicht"
                    onChange={(e) => setName(e.target.value)}
                />

                <DateTimePicker onChange={setStartTime}
                                label="Beginn" placeholder="Pick date and time" />
                <DateTimePicker onChange={setEndTime}
                                label="Ende" placeholder="Pick date and time" />

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
        </Center>

    )
}