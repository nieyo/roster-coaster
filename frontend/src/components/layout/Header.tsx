import {useNavigate} from "react-router-dom";

export default function Header() {

    const navigate = useNavigate();

    return (
                <Group>
                    <Button onClick={() => navigate("add")}>Neue Schicht</Button>
                    <Button onClick={() => navigate("/")}>Alle Schichten</Button>
                </Group>
    )
}