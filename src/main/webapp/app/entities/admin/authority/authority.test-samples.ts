import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '5ef40c6d-c59d-4d51-8421-48ce30d39e45',
};

export const sampleWithPartialData: IAuthority = {
  name: '95a274cd-924c-434e-92b8-e7ed2bd06b9f',
};

export const sampleWithFullData: IAuthority = {
  name: '959c1bc6-775d-421d-9c65-fba9b6dbb994',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
