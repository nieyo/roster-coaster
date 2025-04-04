import {ActionIcon, Group, Pill, Stack, TextInput} from "@mantine/core";
import {Shift, User} from "../../types/types.ts";
import {ChangeEvent, useState} from "react";

interface VolunteerGalleryProps {
    shift: Shift
    handleUpdate: (id: string, shiftToSave: Shift) => void
}

export default function VolunteerGallery(props: Readonly<VolunteerGalleryProps>) {

    const [name, setName] = useState<string>("");

    const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
        setName(event.target.value);
    };

    const handleAddParticipant = () => {
        if (name.trim() === "") return;

        const newParticipant: User = {
            name: name,
        };

        const updatedShift: Shift = {
            ...props.shift,
            participants: [...props.shift.participants, newParticipant]
        };

        props.handleUpdate(props.shift.id, updatedShift);

        setName("");
    };

    const pills = props.shift.participants
        .map((user) => (
            <Pill key={user.name}>
                {user.name}
            </Pill>
        ));

    return (
        <Stack>
            <Group>
                {
                    props.shift.participants && props.shift.participants.length > 0
                        ? <Pill.Group>{pills}</Pill.Group>
                        : <Pill>Keine Helfer</Pill>
                }
            </Group>

            {/*TODO prevent duplicate inputs and maybe use something different than user.name as id in ShiftGallery*/}
            <Group>
                <TextInput
                    size={"xs"}
                    placeholder={"Name"}
                    value={name}
                    onChange={handleChange}
                />
                <ActionIcon size={"md"} color={"green"}
                            onClick={handleAddParticipant}
                ></ActionIcon>
            </Group>
        </Stack>
    )
}