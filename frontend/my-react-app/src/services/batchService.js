const BASE_URL = 'http://localhost:8080/api/batches';

export const getActiveBatchesByCourse = async (courseId) => {
  const response = await fetch(`${BASE_URL}/course/${courseId}/active`);
  if (!response.ok) throw new Error('Failed to fetch active batches');
  return await response.json();
};