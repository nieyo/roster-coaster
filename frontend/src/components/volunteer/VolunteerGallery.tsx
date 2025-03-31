import {Badge, CloseButton, Group, Stack} from "@mantine/core";
import {Shift} from "../../types/types.ts";

interface VolunteerGalleryProps {
    shift: Shift
}

export default function VolunteerGallery(props: Readonly<VolunteerGalleryProps>) {

    return (
        <Stack>
            {
                props.shift.volunteers
                    ? props.shift.volunteers.map((user) => (
                        <Group key={props.shift.id}>
                            <Badge>{user.name}</Badge>
                            <CloseButton/>
                        </Group>
                    ))
                    : <Badge>Keine Helfer</Badge>
            }
        </Stack>
    )
}