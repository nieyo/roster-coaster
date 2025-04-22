import {ShiftDurationDTO} from "./ShiftDurationDTO.ts";
import {UserDTO} from "./UserDTO.ts";

export interface ShiftDTO {
    id: string;
    duration: ShiftDurationDTO;
    participants: UserDTO[];
    minParticipants: number;
    maxParticipants: number;
}