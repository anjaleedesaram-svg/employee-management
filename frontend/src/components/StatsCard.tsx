import './StatsCard.scss';

interface Props {
  stats: Record<string, number>;
}

export default function StatsCard({ stats }: Props) {
  const getStatusLabel = (key: string) => {
    return key.replace('_', ' ').toLowerCase().replace(/^\w/, c => c.toUpperCase());
  };

  return (
    <div className="stats">
      {Object.entries(stats).map(([key, value]) => (
        <div className="stats__card" key={key}>
          <h3>{value}</h3>
          <p>{getStatusLabel(key)}</p>
        </div>
      ))}
    </div>
  );
}