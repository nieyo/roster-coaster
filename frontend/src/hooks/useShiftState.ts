import {useCallback, useEffect, useState} from 'react';
import axios from 'axios';
import {Shift} from "../types/types.ts";


const useShiftState = () => {
    const [shiftList, setShiftList] = useState<Shift[]>([]);
    const [shiftListIsLoading, setShiftListIsLoading] = useState<boolean>(true);
    const [shiftListError, setShiftListError] = useState<string | null>(null);
    const baseURL = "/api/shift";

    const getShiftList = useCallback(() => {
        setShiftListIsLoading(true);
        axios.get(baseURL)
            .then((response) => {
                setShiftList(convertShift(response.data));
                setShiftListError(null);
            })
            .catch((err) => {
                setShiftListError('Failed to fetch shifts');
                console.error(err);
            })
            .finally(() => {
                setShiftListIsLoading(false);
            });
    }, []);

    useEffect(() => {
        getShiftList();
    }, [getShiftList]);

    const convertShift = (rawShifts: Shift[]): Shift[] => {
        return rawShifts.map(shift => ({
            ...shift,
            startTime: new Date(shift.startTime),
            endTime: new Date(shift.endTime),
        }));
    };

    return {shiftList, shiftListIsLoading, shiftListError, getShiftList};
}

export default useShiftState;
