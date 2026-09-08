import { useEffect, useState } from 'react';

function Step4({ order }) {
    const [status, setStatus] = useState('loading'); // loading | success | error
    const [activationResult, setActivationResult] = useState(null);
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        fetch(`http://localhost:8080/api/orders/${order.id}/activate`, {
            method: 'POST',
        })
            .then(async (res) => {
                if (!res.ok) {
                    const text = await res.text();
                    throw new Error(text || 'Aktivasyon başarısız oldu.');
                }
                return res.json();
            })
            .then((result) => {
                setActivationResult(result);
                setStatus('success');
            })
            .catch((err) => {
                setErrorMessage(err.message);
                setStatus('error');
            });
    }, [order.id]);

    return (
        <div>
            <h2>4. Adım: Sipariş Onayı ve Aktivasyon Durumu</h2>

            <p>Sipariş No: {order.id}</p>

            {status === 'loading' && <p>Hattınız aktive ediliyor, lütfen bekleyin...</p>}

            {status === 'success' && (
                <div>
                    <p style={{ color: 'green' }}>Hattınız başarıyla aktifleştirildi.</p>
                    <p>Durum: {activationResult.status}</p>
                </div>
            )}

            {status === 'error' && (
                <div>
                    <p style={{ color: 'red' }}>Aktivasyon sırasında bir sorun oluştu.</p>
                    <p>{errorMessage}</p>
                </div>
            )}
        </div>
    );
}

export default Step4;