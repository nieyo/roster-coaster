import {ShiftDurationDTO} from "./ShiftDurationDTO.ts";
import {UserDTO} from "./UserDTO.ts";

export interface CreateShiftDTO {
    duration: ShiftDurationDTO;
    participants: UserDTO[];
}