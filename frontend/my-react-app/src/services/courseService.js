const BASE_URL = 'http://localhost:8080/api/courses';

export const getActiveCourses = async () => {
  const response = await fetch(`${BASE_URL}/active`);
  if (!response.ok) throw new Error('Failed to fetch active courses');
  return await response.json();
};

export const getCourseById = async (id) => {
  const response = await fetch(`${BASE_URL}/${id}`);
  if (!response.ok) throw new Error('Failed to fetch course details');
  return await response.json();
};