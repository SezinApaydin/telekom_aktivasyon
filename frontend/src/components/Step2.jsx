import { useEffect, useState } from 'react';

function Step2({ cartId, onNext, onBack }) {
    const [devices, setDevices] = useState([]);
    const [cartItems, setCartItems] = useState([]);
    const [selectedDeviceId, setSelectedDeviceId] = useState(null);

    const loadCartItems = () => {
        fetch(`http://localhost:8080/api/cart/${cartId}/items`)
            .then((res) => res.json())
            .then(setCartItems);
    };

    useEffect(() => {
        fetch('http://localhost:8080/api/devices')
            .then((res) => res.json())
            .then(setDevices);

        loadCartItems();
    }, []);

    const selectDevice = (device) => {
        fetch(`http://localhost:8080/api/cart/${cartId}/items/device/${device.id}`, {
            method: 'POST',
        })
            .then((res) => {
                if (!res.ok) throw new Error('Cihaz eklenemedi');
                setSelectedDeviceId(device.id);
                loadCartItems();
            })
            .catch(() => alert('Bu cihaz stokta olmayabilir.'));
    };

    const renderItemLabel = (item) => {
        if (item.itemType === 'package') return `Paket: ${item.packageEntity.name} (${item.packageEntity.monthlyPrice} TL/ay)`;
        if (item.itemType === 'device') return `Cihaz: ${item.device.brand} ${item.device.model} (${item.device.price} TL)`;
        if (item.itemType === 'sim') return `Numara: ${item.simCard.msisdn}`;
        return '';
    };

    return (
        <div>
            <h2>2. Adım: Cihaz Ekle (Opsiyonel)</h2>

            <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
                {devices.map((device) => (
                    <div
                        key={device.id}
                        onClick={() => selectDevice(device)}
                        style={{
                            border: selectedDeviceId === device.id ? '2px solid green' : '1px solid #ccc',
                            borderRadius: 8,
                            padding: 12,
                            cursor: 'pointer',
                            width: 160,
                        }}
                    >
                        <strong>{device.brand} {device.model}</strong>
                        <p>{device.price} TL</p>
                        <p>Stok: {device.stockQuantity}</p>
                    </div>
                ))}
            </div>

            <h3 style={{ marginTop: 30 }}>Sepet Özeti</h3>
            <ul>
                {cartItems.map((item) => (
                    <li key={item.id}>{renderItemLabel(item)}</li>
                ))}
            </ul>

            <div style={{ marginTop: 20, display: 'flex', gap: 12 }}>
                <button onClick={onBack} style={{ padding: '10px 20px', cursor: 'pointer' }}>
                    ← Geri
                </button>
                <button onClick={onNext} style={{ padding: '10px 20px', cursor: 'pointer' }}>
                    İleri →
                </button>
            </div>
        </div>
    );
}

export default Step2;