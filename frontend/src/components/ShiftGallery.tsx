import {Card, Group, Stack, Text} from "@mantine/core";
import ShiftCard from "./ShiftCard.tsx";

export default function ShiftGallery() {

    return (
        <Stack>
            <Card>
                <Text>Schicht 1</Text>
                <Group>
                    <ShiftCard/>
                    <ShiftCard/>
                    <ShiftCard/>
                    <ShiftCard/>
                    <ShiftCard/>
                </Group>
            </Card>
            <Card>
                <Text>Schicht 2</Text>
                <Group>
                    <ShiftCard/>
                    <ShiftCard/>
                    <ShiftCard/>
                    <ShiftCard/>
                    <ShiftCard/>
                </Group>
            </Card>
        </Stack>
    )
}