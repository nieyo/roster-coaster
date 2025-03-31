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
        axios.delete(`${baseURL}/${id}`)
            .then(() => {
                getShiftList()
            })
            .catch((error) => {
                console.error("Error deleting movie:", error);
                alert("Failed to delete the movie. Please try again.");
            });
    };

    useEffect(() => {
        getShiftList();
    }, [getShiftList]);

    return {shiftList, shiftListIsLoading, shiftListError, getShiftList, deleteShift};
}

export default useShiftState;
