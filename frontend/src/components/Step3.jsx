import { useState } from 'react';

function Step3({ cartId, onBack, onOrderCreated }) {
    const [city, setCity] = useState('');
    const [district, setDistrict] = useState('');
    const [addressLine, setAddressLine] = useState('');
    const [postalCode, setPostalCode] = useState('');
    const [customerName, setCustomerName] = useState('');
    const [tckn, setTckn] = useState('');
    const [message, setMessage] = useState('');

    const validate = () => {
        if (!customerName.trim() || !city.trim() || !district.trim() || !addressLine.trim()) {
            return 'Lütfen tüm alanları doldurun.';
        }
        if (!/^\d{11}$/.test(tckn)) {
            return 'TCKN tam olarak 11 haneli rakamlardan oluşmalı.';
        }
        if (!/^\d{5}$/.test(postalCode)) {
            return 'Posta kodu tam olarak 5 haneli rakamlardan oluşmalı.';
        }
        return null;
    };

    const submitOrder = () => {
        const validationError = validate();
        if (validationError) {
            setMessage(validationError);
            return;
        }
        setMessage('');

        fetch('http://localhost:8080/api/addresses', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ city, district, addressLine, postalCode }),
        })
            .then((res) => {
                if (!res.ok) throw new Error('Adres kaydedilemedi');
                return res.json();
            })
            .then((address) => {
                return fetch('http://localhost:8080/api/checkout', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        cartId,
                        addressId: address.id,
                        customerName,
                        tckn,
                    }),
                });
            })
            .then((res) => {
                if (!res.ok) throw new Error('Sipariş oluşturulamadı, bilgileri kontrol et');
                return res.json();
            })
            .then((createdOrder) => {
                onOrderCreated(createdOrder);
            })
            .catch((err) => setMessage(err.message));
    };

    return (
        <div>
            <h2>3. Adım: Adres ve Kimlik Bilgileri</h2>

            {message && <p style={{ color: 'red' }}>{message}</p>}

            <div style={{ display: 'flex', flexDirection: 'column', gap: 10, maxWidth: 300 }}>
                <input placeholder="Ad Soyad" value={customerName} onChange={(e) => setCustomerName(e.target.value)} />
                <input placeholder="TCKN" value={tckn} onChange={(e) => setTckn(e.target.value)} />
                <input placeholder="Şehir" value={city} onChange={(e) => setCity(e.target.value)} />
                <input placeholder="İlçe" value={district} onChange={(e) => setDistrict(e.target.value)} />
                <input placeholder="Adres" value={addressLine} onChange={(e) => setAddressLine(e.target.value)} />
                <input placeholder="Posta Kodu" value={postalCode} onChange={(e) => setPostalCode(e.target.value)} />
            </div>

            <div style={{ marginTop: 20, display: 'flex', gap: 12 }}>
                <button onClick={onBack} style={{ padding: '10px 20px', cursor: 'pointer' }}>
                    ← Geri
                </button>
                <button onClick={submitOrder} style={{ padding: '10px 20px', cursor: 'pointer' }}>
                    Başvuruyu Tamamla
                </button>
            </div>
        </div>
    );
}

export default Step3;