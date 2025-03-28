import {Card, Group, Stack, Text} from "@mantine/core";
import ShiftCard from "./ShiftCard.tsx";
import {Shift} from "../../types/types.ts";

interface ShiftGalleryProps {
    shifts: Shift[]
}

export default function ShiftGallery(props: ShiftGalleryProps) {

    return (
        props.shifts && props.shifts.length > 0
            ? <Stack>
                <Card>
                    <Text>Task 1</Text>
                    <Group>
                        {
                            props.shifts.map((shift) => (
                                <ShiftCard key={shift.id} shift={shift}/>
                            ))
                        }
                    </Group>
                </Card>
            </Stack>
            : <Text>Keine Schichten vorhanden</Text>
    )
}