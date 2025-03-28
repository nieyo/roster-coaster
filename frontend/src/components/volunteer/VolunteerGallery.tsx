import {Badge, CloseButton, Group, Stack} from "@mantine/core";
import {Shift} from "../../types/types.ts";

interface VolunteerGalleryProps {
    shift: Shift
}

export default function VolunteerGallery(props: VolunteerGalleryProps) {

    return (
        <Stack>
            {
                props.shift.volunteers
                    ? props.shift.volunteers.map((user) => (
                        <Group>
                            <Badge>{user.name}</Badge>
                            <CloseButton/>
                        </Group>
                    ))
                    : <Badge>Keine Helfer</Badge>
            }
        </Stack>
    )
}