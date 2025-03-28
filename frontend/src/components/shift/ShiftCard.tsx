import VolunteerGallery from "../volunteer/VolunteerGallery.tsx";
import {Card, Group, Text} from "@mantine/core";

export default function ShiftCard() {

    return (
            <Card>
                <Text>12 - 14 Uhr</Text>
                <Group>
                    <VolunteerGallery/>
                </Group>
            </Card>
    )
}