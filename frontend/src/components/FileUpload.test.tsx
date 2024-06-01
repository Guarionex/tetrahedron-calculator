import axios from "axios"
import {fireEvent, render, screen, waitFor} from "@testing-library/react"
import {FileUpload} from "./FileUpload"
import {userEvent} from "@testing-library/user-event"


jest.mock('axios')
const mockedAxios = axios as jest.Mocked<typeof axios>

describe('FileUpload', () => {
    beforeEach(() => {
        mockedAxios.post.mockClear()
        mockedAxios.get.mockClear()
    })

    it('renders the component', () => {
        render(<FileUpload/>)
        expect(screen.getByText('Tetrahedron Calculator')).toBeInTheDocument()
        expect(screen.getByRole('button', {name: 'Upload and Calculate'})).toBeInTheDocument()
    })

    it('handles file upload and calculation', async () => {
        const fileContent = `
      (3.00, 4.00, 5.00, 22)
      (2.00, 3.00, 3.00, 3)
      (1.00, 2.00, 2.00, 4)
      (3.50, 4.50, 5.50, 14)
      (2.50, 3.50, 3.50, 24)
      (6.70, 32.20, 93.0, 5)
      (2.50, 3.00, 7.00, 40)
    `
        const file = new File([fileContent], 'points.txt', { type: 'text/plain' })

        mockedAxios.post.mockResolvedValue({
            headers: { location: 'http://localhost:8080/api/points/1' },
        })
        mockedAxios.get.mockResolvedValue({
            data: { indices: [0, 3, 4, 6] },
        })

        render(<FileUpload />)

        const fileInput = screen.getByLabelText('Upload File')
        fireEvent.change(fileInput, { target: { files: [file] } })

        const uploadButton = screen.getByRole('button', { name: 'Upload and Calculate' })
        fireEvent.click(uploadButton)

        await waitFor(() => expect(mockedAxios.post).toHaveBeenCalledTimes(1))
        await waitFor(() => expect(mockedAxios.get).toHaveBeenCalledTimes(1))

        expect(screen.getByText(/Smallest Tetrahedron Indices: 0, 3, 4, 6/i)).toBeInTheDocument()
    })

    it('displays an error message when upload fails', async () => {
        const fileContent = `
      (3.00, 4.00, 5.00, 22)
      (2.00, 3.00, 3.00, 3)
      (1.00, 2.00, 2.00, 4)
      (3.50, 4.50, 5.50, 14)
      (2.50, 3.50, 3.50, 24)
      (6.70, 32.20, 93.0, 5)
      (2.50, 3.00, 7.00, 40)
    `
        const file = new File([fileContent], 'points.txt', { type: 'text/plain' })

        mockedAxios.post.mockRejectedValue(new Error('Network Error'))

        render(<FileUpload />)

        const fileInput = screen.getByLabelText('Upload File')
        fireEvent.change(fileInput, { target: { files: [file] } })

        const uploadButton = screen.getByRole('button', { name: 'Upload and Calculate' })
        fireEvent.click(uploadButton)

        await waitFor(() => expect(mockedAxios.post).toHaveBeenCalledTimes(1))

        expect(screen.getByText(/Error uploading file:/i)).toBeInTheDocument()
    })
})