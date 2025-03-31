import VolunteerGallery from "../volunteer/VolunteerGallery.tsx";
import {Card, CloseButton, Group, Text} from "@mantine/core";
import {Shift} from "../../types/types.ts";

interface ShiftCardProps {
    shift: Shift,
    handleDelete: (id: string) => void
}

export default function ShiftCard(props: Readonly<ShiftCardProps>) {

    return (
        <Card
            withBorder
            shadow={"xl"}
            padding={"lg"}
            radius={"md"}
        >
            <Group>
                <Text>
                    {props.shift.startTime.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'}) + " - " + props.shift.endTime.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'})}
                </Text>
                <CloseButton onClick={() => props.handleDelete(props.shift.id)}/>
            </Group>
            <Group>
                <VolunteerGallery shift={props.shift}/>
            </Group>
        </Card>
    )
}