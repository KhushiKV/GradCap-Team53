import axios from 'axios'
import React, { useEffect } from 'react'

export default function Feeadback() {

    const [feedback, setFeedback] = React.useState([])
    const [loading, setLoading] = React.useState(true)

    useEffect(() => {
        const token = localStorage.getItem('token')
        axios.get(`${process.env.REACT_APP_API_URL}/feedbacks`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
            }
        }).then(res => {
            console.log(res.data)
            setFeedback(res.data)
            setLoading(false)
        }
        ).catch(err => {
            console.log(err.response.data)
            setLoading(false)
        })
    }, [])

    return (
        <>
            <div className='p-3 h5 text-secondary'>Feedbacks</div>
            {
                loading ? <div className="text-center mt-5">
                    <div className="spinner-border text-primary me-2" role="status">
                    </div>
                    <span className="sr-only text-primary fs-3 fw-bold">Loading...</span>
                </div> :
                    feedback.length === 0 ? <div className="text-center mt-5">
                        <span className="sr-only text-secondary">No Feedbacks</span>
                    </div>
                        :
                        <div className="ul">
                            {
                                feedback.map(feedback => {
                                    return (
                                        <div className="li my-2" key={feedback.id}>
                                            <div className="card">
                                                <div className="card-body">
                                                    <h5 className="card-title">{feedback.name}</h5>
                                                    <h6 className="card-subtitle mb-2 text-muted">{feedback.email}</h6>
                                                    <p className="card-text">{feedback.feedback}</p>

                                                </div>
                                            </div>
                                        </div>
                                    )
                                }
                                )
                            }
                        </div>

            }
        </>

    )
}
