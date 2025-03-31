import {useCallback, useEffect, useState} from 'react';
import axios from 'axios';
import {Shift} from "../types/types.ts";


const useShiftState = () => {
    const [shiftList, setShiftList] = useState<Shift[]>([]);
    const [shiftListIsLoading, setShiftListIsLoading] = useState<boolean>(true);
    const [shiftListError, setShiftListError] = useState<string | null>(null);
    const baseURL = "/api/shift";

    const convertShift = (rawShifts: Shift[]): Shift[] => {
        return rawShifts.map(shift => ({
            ...shift,
            startTime: new Date(shift.startTime),
            endTime: new Date(shift.endTime),
            // participants: [...(shift.participants || [])]
        }));
    };

    const getShiftList = useCallback(() => {
        setShiftListIsLoading(true);
        axios.get(baseURL)
            .then((response) => {
                setShiftList(convertShift(response.data));
                setShiftListError(null);
            })
            .catch(() => setShiftListError('Failed to fetch shifts'))
            .finally(() => setShiftListIsLoading(false));
    }, []);

    const deleteShift = (id: string) => {
        setShiftListIsLoading(true)
        axios.delete(`${baseURL}/${id}`)
            .then(() => {
                getShiftList()
            })
            .catch((error) => {
                console.error("Error deleting Shift:", error);
                alert("Failed to delete. Please try again.");
            })
            .finally(() =>
                setShiftListIsLoading(false)
            )
    };

    const updateShift = (id: string, shiftToUpdate: Shift) => {
        setShiftListIsLoading(true)
        axios.put(`${baseURL}/${id}`, shiftToUpdate)
            .then(() => {
                getShiftList()
            })
            .catch((error) => {
                setShiftListError(error.message);
            })
            .finally(() => {
                setShiftListIsLoading(false)
            })
    };

    useEffect(() => {
        getShiftList();
    }, [getShiftList]);

    return {shiftList, shiftListIsLoading, shiftListError, getShiftList, deleteShift, updateShift};
}

export default useShiftState;
