import {Alert, Box, Button, Container, TextField, Typography} from "@mui/material"
import {useState} from "react"
import axios from "axios";

interface Point {
    x: number,
    y: number,
    z: number,
    n: number
}

export const FileUpload: React.FC = () => {

    const [loading, setLoading] = useState(false)
    const [tetrahedron, setTetrahedron] = useState<number[]>([])
    const [error, setError] = useState<string | null>(null)
    const [file, setFile] = useState<File | null>(null)
    const [points, setPoints] = useState<Point[]>([])

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (event.target.files && event.target.files[0]) {
            setFile(event.target.files[0])
        }
    }

    const handleUpload = async () => {
        if (!file) return

        const reader = new FileReader()
        reader.onload = async (e) => {
            const text = e.target?.result as string
            const pointsArray: Point[] = text
                .split('\n')
                .filter((line) => line.trim() !== '')
                .map((line) => {
                    const [x, y, z, n] = line
                        .replace(/[()]/g, '')
                        .split(',')
                        .map((num) => parseFloat(num))
                    return { x, y, z, n }
                })
            setPoints(pointsArray)

            try {
                setLoading(true)
                setError(null)
                const response = await axios.post('http://localhost:8080/api/points', points)
                const location = response.headers['location']
                const tetrahedronResponse = await axios.get(`${location}/tetrahedron`)
                setTetrahedron(tetrahedronResponse.data)
            } catch (error) {
                console.error('Error uploading file:', error);
                if (axios.isAxiosError(error)) {
                    setError('Error uploading file: ' + error.message);
                } else {
                    setError('Error uploading file: ' + String(error));
                }
            } finally {
                setLoading(false)
            }
        }
        reader.readAsText(file)
    }

    return <Container>
        <Box my={4}>
            <Typography variant={"h4"} component={"h1"} gutterBottom>
                Tetrahedron Calculator
            </Typography>
            <TextField
                type={"file"}
                inputProps={{ accept: '.txt'}}
                onChange={handleFileChange}
                fullWidth
                label={"Upload File"}
                />
            <Button variant={"contained"} color={"primary"} onClick={handleUpload} disabled={loading}>
                Upload and Calculate
            </Button>
            {
                tetrahedron.length > 0 && (
                    <Typography variant={"h6"} component={"p"} gutterBottom>
                        Smallest Tetrahedron Indices: {tetrahedron.join(', ')}
                    </Typography>
                )
            }
            {error && <Alert severity={"error"}>{error}</Alert>}
        </Box>
    </Container>
}