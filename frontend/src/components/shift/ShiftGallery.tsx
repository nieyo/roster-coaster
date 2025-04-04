import ShiftCard from "./ShiftCard.tsx";
import {Shift} from "../../types/types.ts";

interface ShiftGalleryProps {
    shifts: Shift[],
    handleDelete: (id: string) => void
    handleUpdate: (id: string, shiftToSave: Shift) => void
}

export default function ShiftGallery(props: Readonly<ShiftGalleryProps>) {

    return (
        props.shifts && props.shifts.length > 0
            ? <Stack>
                <Card>
                    <Group>
                        <Text>Essensausgabe | 17. März</Text>
                    </Group>
                    <Space h="md" />
                    <Group>
                        {
                            props.shifts.map((shift) => (
                                <ShiftCard
                                    key={shift.id}
                                    shift={shift}
                                    handleDelete={props.handleDelete}
                                    handleUpdate={props.handleUpdate}
                                />
                            ))
                        }
                    </Group>
                </Card>
            </Stack>
            : <Text>Keine Schichten vorhanden</Text>
    )
}