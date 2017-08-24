(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('resources', {
            parent: 'entity',
            url: '/resources?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Resources'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resources/resources.html',
                    controller: 'ResourcesController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('resources-detail', {
            parent: 'resources',
            url: '/resources/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Resources'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resources/resources-detail.html',
                    controller: 'ResourcesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Resources', function($stateParams, Resources) {
                    return Resources.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'resources',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('resources-detail.edit', {
            parent: 'resources-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resources/resources-dialog.html',
                    controller: 'ResourcesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Resources', function(Resources) {
                            return Resources.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resources.new', {
            parent: 'resources',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resources/resources-dialog.html',
                    controller: 'ResourcesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                desc: null,
                                icon: null,
                                iconContentType: null,
                                resourceUrl: null,
                                resourceType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('resources', null, { reload: 'resources' });
                }, function() {
                    $state.go('resources');
                });
            }]
        })
        .state('resources.edit', {
            parent: 'resources',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resources/resources-dialog.html',
                    controller: 'ResourcesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Resources', function(Resources) {
                            return Resources.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resources', null, { reload: 'resources' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resources.delete', {
            parent: 'resources',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resources/resources-delete-dialog.html',
                    controller: 'ResourcesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Resources', function(Resources) {
                            return Resources.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resources', null, { reload: 'resources' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
