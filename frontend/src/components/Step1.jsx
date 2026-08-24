import { useEffect, useState } from 'react';

function Step1({ cartId, onNext }) {
    const [packages, setPackages] = useState([]);
    const [simCards, setSimCards] = useState([]);
    const [selectedPackageId, setSelectedPackageId] = useState(null);
    const [selectedSimId, setSelectedSimId] = useState(null);
    const [message, setMessage] = useState('');

    useEffect(() => {
        fetch('http://localhost:8080/api/packages')
            .then((res) => res.json())
            .then(setPackages);

        fetch('http://localhost:8080/api/sim-cards')
            .then((res) => res.json())
            .then(setSimCards);
    }, []);

    const selectPackage = (pkg) => {
        fetch(`http://localhost:8080/api/cart/${cartId}/items/package/${pkg.id}`, {
            method: 'POST',
        })
            .then((res) => {
                if (!res.ok) throw new Error('Paket eklenemedi');
                return res.json();
            })
            .then(() => {
                setSelectedPackageId(pkg.id);
                setMessage('');
            })
            .catch(() => setMessage('Paket eklenirken bir hata oluştu.'));
    };

    const selectSimCard = (sim) => {
        if (selectedSimId) {
            setMessage('Zaten bir numara seçtin, değiştirmek için sayfayı yenile.');
            return;
        }
        fetch(`http://localhost:8080/api/cart/${cartId}/items/sim/${sim.id}`, {
            method: 'POST',
        })
            .then((res) => {
                if (!res.ok) throw new Error('Numara eklenemedi');
                return res.json();
            })
            .then(() => {
                setSelectedSimId(sim.id);
                setMessage('');
            })
            .catch(() => setMessage('Bu numara artık müsait olmayabilir, başka bir numara seç.'));
    };

    return (
        <div>
            <h2>1. Adım: Paket ve Numara Seç</h2>

            {message && <p style={{ color: 'red' }}>{message}</p>}

            <h3>Paketler</h3>
            <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
                {packages.map((pkg) => (
                    <div
                        key={pkg.id}
                        onClick={() => selectPackage(pkg)}
                        style={{
                            border: selectedPackageId === pkg.id ? '2px solid green' : '1px solid #ccc',
                            borderRadius: 8,
                            padding: 12,
                            cursor: 'pointer',
                            width: 160,
                        }}
                    >
                        <strong>{pkg.name}</strong>
                        <p>{pkg.monthlyPrice} TL/ay</p>
                        <p>{pkg.dataQuotaGb} GB - {pkg.minutes} dk</p>
                    </div>
                ))}
            </div>

            <h3>Numara Seç</h3>
            <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
                {simCards.map((sim) => (
                    <div
                        key={sim.id}
                        onClick={() => selectSimCard(sim)}
                        style={{
                            border: selectedSimId === sim.id ? '2px solid green' : '1px solid #ccc',
                            borderRadius: 8,
                            padding: 12,
                            cursor: 'pointer',
                            width: 160,
                        }}
                    >
                        {sim.msisdn}
                    </div>
                ))}
            </div>

            {selectedPackageId && selectedSimId && (
                <div style={{ marginTop: 20 }}>
                    <p style={{ color: 'green' }}>Paket ve numara seçildi.</p>
                    <button onClick={onNext} style={{ padding: '10px 20px', cursor: 'pointer' }}>
                        İleri →
                    </button>
                </div>
            )}
        </div>
    );
}

export default Step1;