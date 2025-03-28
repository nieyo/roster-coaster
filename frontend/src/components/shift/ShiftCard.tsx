import VolunteerGallery from "../volunteer/VolunteerGallery.tsx";
import {Card, Group, Text} from "@mantine/core";
import {Shift} from "../../types/types.ts";

interface ShiftCardProps {
    shift: Shift
}

export default function ShiftCard(props: ShiftCardProps) {

    return (
            <Card>
                <Text>
                    {props.shift.startTime.toLocaleDateString()}
                </Text>
                <Text>
                    {props.shift.startTime.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} -
                    {props.shift.endTime.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                </Text>
                <Group>
                    <VolunteerGallery shift={props.shift}/>
                </Group>
            </Card>
    )
}