import {ActionIcon, Badge, Group, Stack, TextInput} from "@mantine/core";
import {Shift, User} from "../../types/types.ts";
import {ChangeEvent, useState} from "react";
import {IconCheck} from "@tabler/icons-react";

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

    return (
        <Stack>
            <Group>
                {
                    props.shift.participants && props.shift.participants.length > 0
                        ? props.shift.participants.map((user) => (
                            <Badge key={user.name}>
                                {user.name}
                            </Badge>
                        ))
                        : <Badge>
                            Keine Helfer
                    </Badge>
                }
            </Group>
            <Group>
                <TextInput
                    size={"xs"}
                    placeholder={"Name"}
                    value={name}
                    onChange={handleChange}
                />
                <ActionIcon size={"md"} color={"green"}
                            onClick={handleAddParticipant}
                >
                    <IconCheck size={"12"}/>
                </ActionIcon>
            </Group>


        </Stack>
    )
}