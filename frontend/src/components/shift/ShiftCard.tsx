import VolunteerGallery from "../volunteer/VolunteerGallery.tsx";
import {ActionIcon, Card, CloseButton, Group, Space, Text} from "@mantine/core";
import {Shift} from "../../types/types.ts";
import {IconPencil} from "@tabler/icons-react";
import {useNavigate} from "react-router-dom";

interface ShiftCardProps {
    shift: Shift,
    handleDelete: (id: string) => void
    handleUpdate: (id: string, shiftToSave: Shift) => void
}

export default function ShiftCard(props: Readonly<ShiftCardProps>) {

    const navigate = useNavigate();

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

                {/*TODO create edit shift functionality*/}
                <ActionIcon size={"md"} variant="transparent"
                            onClick={() => navigate("/add")}
                >
                    <IconPencil size={"18"}/>
                </ActionIcon>
                <CloseButton onClick={() => props.handleDelete(props.shift.id)}/>

            </Group>
            <Space h={"md"}/>
            <Group>
                <VolunteerGallery
                    shift={props.shift}
                    handleUpdate={props.handleUpdate}
                />
            </Group>
        </Card>
    )
}